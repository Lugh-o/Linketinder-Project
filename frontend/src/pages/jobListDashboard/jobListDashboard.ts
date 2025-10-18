import styles from "./jobListDashboard.module.css";

import type { Company } from "../../types/Company";
import { companyHeader } from "../../components/header/companyHeader";
import { createNewJobButton } from "./jobListDashboardHelper";
import { renderJobList } from "./jobListDashboardHelper";

export function jobListDashboard(company: Company): HTMLDivElement {
	const container = document.createElement("div");
	container.className = styles.jobListDashboard;

	container.appendChild(companyHeader(company));
	container.appendChild(createNewJobButton(company));
	container.appendChild(renderJobList(company));

	return container;
}
