import { candidateDashboard } from "../pages/candidateDashboard/candidateDashboard";
import { companyDashboard } from "../pages/companyDashboard/companyDashboard";
import { jobListDashboard } from "../pages/jobListDashboard/jobListDashboard";
import { registrationScreen } from "../pages/registrationScreen/registrationScreen";
import type { Store } from "../persistency/Store";
import type { Candidate } from "../types/Candidate";
import type { Company } from "../types/Company";
import type { AppContext } from "./AppContext";

export class Router {
	private navigateTo(screen: HTMLElement): void {
		const appContainer: HTMLBodyElement | null =
			document.querySelector("body");
		if (appContainer) {
			appContainer.innerHTML = "";
			appContainer.appendChild(screen);
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
