import styles from "./jobListDashboard.module.css";

import type { Company } from "../../types/Company";
import { companyHeader } from "../../components/header/companyHeader";
import { createNewJobButton } from "./jobListDashboardHelper";
import { renderJobList } from "./jobListDashboardHelper";
import type { AppContext } from "../../utils/AppContext";

export async function jobListDashboard(
	company: Company,
	appContext: AppContext
): Promise<HTMLDivElement> {
	const container = document.createElement("div");
	container.className = styles.jobListDashboard;

	container.appendChild(companyHeader(company, appContext));
	container.appendChild(createNewJobButton(company, appContext));

	const jobListContainer = document.createElement("div");
	jobListContainer.className = styles.jobListContainer;
	container.appendChild(jobListContainer);

	try {
		const realJobList = await renderJobList(company, appContext);
		jobListContainer.replaceWith(realJobList);
	} catch (error) {
		console.error("Failed to load company jobs:", error);
	}
	return container;
}
