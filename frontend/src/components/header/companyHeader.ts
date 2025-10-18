import styles from "./header.module.css";

import { Company } from "../../types/Company";
import { registrationScreen } from "../../pages/registrationScreen/registrationScreen";
import { navigateTo } from "../../utils/router";
import { jobListDashboard } from "../../pages/jobListDashboard/jobListDashboard";
import { companyDashboard } from "../../pages/companyDashboard/companyDashboard";
import { buildHeader } from "./headerHelper";
import { createHeaderButton, buildCommonAppName } from "./headerHelper";

export function companyHeader(company: Company): HTMLElement {
	return buildHeader(company, buildAppName);
}

function buildAppName(company: Company): HTMLHeadingElement {
	const appName = buildCommonAppName();

	const logoutButton = createHeaderButton("Logout", styles.logoutButton, () =>
		navigateTo(registrationScreen())
	);
	const candidatesButton = createHeaderButton(
		"Ver Candidatos",
		styles.jobsButton,
		() => navigateTo(companyDashboard(company))
	);
	const jobsButton = createHeaderButton("Ver Vagas", styles.jobsButton, () =>
		navigateTo(jobListDashboard(company))
	);

	appName.appendChild(candidatesButton);
	appName.appendChild(jobsButton);
	appName.appendChild(logoutButton);

	return appName;
}
