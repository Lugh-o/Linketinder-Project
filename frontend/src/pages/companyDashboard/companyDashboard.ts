import styles from "./companyDashboard.module.css";

import type { Company } from "../../types/Company";
import type { AppContext } from "../../utils/AppContext";
import { CompanyDashboardFacade } from "./companyDashboardFacade";

export async function companyDashboard(
	company: Company,
	appContext: AppContext
): Promise<HTMLDivElement> {
	const container: HTMLDivElement = document.createElement("div");
	container.className = styles.companyDashboardContainer;

	const companyDashboard = document.createElement("div");
	container.appendChild(companyDashboard);

	try {
		const facade = new CompanyDashboardFacade(appContext);
		const realCompanyDashboard = await facade.render(company);
		companyDashboard.replaceWith(realCompanyDashboard);
	} catch (error) {
		console.error("Failed to load company dashboard:", error);
	}

	return container;
}
