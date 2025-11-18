import { candidateDashboard } from "../pages/candidateDashboard/candidateDashboard";
import { companyDashboard } from "../pages/companyDashboard/companyDashboard";
import { jobListDashboard } from "../pages/jobListDashboard/jobListDashboard";
import { registrationScreen } from "../pages/registrationScreen/registrationScreen";
import type { Candidate } from "../types/Candidate";
import type { Company } from "../types/Company";
import type { AppContext } from "./AppContext";

export class Router {
	private appContainer: HTMLElement;

	constructor() {
		const container = document.querySelector("body");
		if (!container) {
			throw new Error("Could not find <body> element");
		}
		this.appContainer = container as HTMLElement;
	}

	private async navigateTo(
		screen: HTMLDivElement | Promise<HTMLDivElement>
	): Promise<void> {
		try {
			const element = screen instanceof Promise ? await screen : screen;
			this.appContainer.innerHTML = "";
			this.appContainer.appendChild(element);
		} catch (error) {
			console.error("Navigation failed:", error);
		}
	}

	goToRegistration(appContext: AppContext): void {
		this.navigateTo(registrationScreen(appContext));
	}

	goToCompanyDashboard(company: Company, appContext: AppContext) {
		this.navigateTo(companyDashboard(company, appContext));
	}

	goToCandidateDashboard(candidate: Candidate, appContext: AppContext) {
		this.navigateTo(candidateDashboard(candidate, appContext));
	}

	goToJobList(company: Company, appContext: AppContext) {
		this.navigateTo(jobListDashboard(company, appContext));
	}
}
