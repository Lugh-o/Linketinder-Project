import styles from "./header.module.css";

import { Company } from "../../types/Company";
import CompanyIcon from "../../assets/company.svg";
import { registrationScreen } from "../../pages/registrationScreen/registrationScreen";
import { navigateTo } from "../../utils/router";
import { jobListDashboard } from "../../pages/jobListDashboard/jobListDashboard";
import { companyDashboard } from "../../pages/companyDashboard/companyDashboard";

export function companyHeader(company: Company): HTMLElement {
	const container: HTMLElement = document.createElement("header");
	container.className = styles.headerContainer;

	const appName: HTMLHeadingElement = document.createElement("h1");
	appName.className = styles.appName;
	const title1: HTMLElement = document.createElement("span");
	title1.textContent = "Linke";
	title1.className = styles.title1;

	const title2: HTMLElement = document.createElement("span");
	title2.textContent = "Tinder";
	title2.className = styles.title2;

	appName.appendChild(title1);
	appName.appendChild(title2);

	const profileWrapper: HTMLDivElement = document.createElement("div");
	profileWrapper.className = styles.profileWrapper;
	const image: HTMLImageElement = document.createElement("img");
	image.src = CompanyIcon;

	const candidatesButton: HTMLButtonElement =
		document.createElement("button");
	candidatesButton.textContent = "Ver Candidatos";
	candidatesButton.className = styles.jobsButton;
	candidatesButton.addEventListener("click", () => {
		navigateTo(companyDashboard(company));
	});

	const jobsButton: HTMLButtonElement = document.createElement("button");
	jobsButton.textContent = "Ver vagas";
	jobsButton.className = styles.jobsButton;
	jobsButton.addEventListener("click", () => {
		navigateTo(jobListDashboard(company));
	});

	appName.appendChild(candidatesButton);
	appName.appendChild(jobsButton);

	profileWrapper.appendChild(image);

	const name: HTMLHeadingElement = document.createElement("h4");
	name.textContent = company.name;
	profileWrapper.appendChild(name);

	const logoutButton: HTMLButtonElement = document.createElement("button");
	logoutButton.textContent = "Logout";
	logoutButton.className = styles.logoutButton;

	logoutButton.addEventListener("click", () => {
		navigateTo(registrationScreen());
	});

	appName.appendChild(logoutButton);

	container.appendChild(appName);
	container.appendChild(profileWrapper);

	return container;
}
