import styles from "./jobCard.module.css";

import type { Job } from "../../types/Job";
import type { Company } from "../../types/Company";
import { navigateTo } from "../../utils/router";
import { jobListDashboard } from "../../pages/jobListDashboard/jobListDashboard";
import { store } from "../../Store";
import {
	createCommonElements,
	addInfoBubble,
	createJobCardButton,
} from "./jobCardHelper";
import deleteIcon from "../../assets/delete.svg";

export function companyJobCard(job: Job, company: Company): HTMLDivElement {
	const {
		container,
		controlsWrapper,
		firstColumn,
		nameAffinityWrapper,
		competencyWrapper,
	} = createCommonElements(job);

	createJobCardButton(
		controlsWrapper,
		deleteIcon,
		styles.dislikeButton,
		() => {
			let index = company.getJobList().indexOf(job);
			company.removeJobByIndex(index);
			store.save(); // Evitar dependencias circulares
			navigateTo(jobListDashboard(company));
		}
	);

	firstColumn.appendChild(nameAffinityWrapper);
	firstColumn.appendChild(competencyWrapper);
	container.appendChild(firstColumn);
	container.appendChild(controlsWrapper);

	addInfoBubble(container, job.description);

	return container;
}
