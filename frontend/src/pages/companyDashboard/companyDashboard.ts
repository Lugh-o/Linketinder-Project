import styles from "./companyDashboard.module.css";

import type { Company } from "../../types/Company";
import type { AppContext } from "../../utils/AppContext";
import { CompanyDashboardFacade } from "./companyDashboardFacade";

export function companyDashboard(
	company: Company,
	appContext: AppContext
): HTMLDivElement {
	const container: HTMLDivElement = document.createElement("div");
	container.className = styles.companyDashboardContainer;
	const facade = new CompanyDashboardFacade(appContext);
	container.appendChild(facade.render(company));
	return container;
}
