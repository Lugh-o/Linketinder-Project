import styles from "./header.module.css";

import { Company } from "../../types/Company";
import { buildHeader } from "./headerHelper";
import { createHeaderButton, buildCommonAppName } from "./headerHelper";
import type { AppContext } from "../../utils/AppContext";

export function companyHeader(
	company: Company,
	appContext: AppContext
): HTMLElement {
	return buildHeader(company, () => buildAppName(appContext, company));
}

function buildAppName(
	appContext: AppContext,
	company: Company
): HTMLHeadingElement {
	const appName = buildCommonAppName();

	appName.appendChild(
		createHeaderButton("Ver Candidatos", styles.jobsButton, () =>
			appContext.router.goToCompanyDashboard(company, appContext)
		)
	);
	appName.appendChild(
		createHeaderButton("Ver Vagas", styles.jobsButton, () =>
			appContext.router.goToJobList(company, appContext)
		)
	);
	appName.appendChild(
		createHeaderButton("Logout", styles.logoutButton, () =>
			appContext.router.goToRegistration(appContext)
		)
	);

	return appName;
}
