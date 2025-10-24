import styles from "./jobCard.module.css";

import type { Job } from "../../types/Job";
import {
	createCommonElements,
	addInfoBubble,
	createJobCardButton,
} from "./jobCardHelper";
import deleteIcon from "../../assets/delete.svg";

export function companyJobCard(
	job: Job,
	jobCardOnClick: () => void
): HTMLDivElement {
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
		jobCardOnClick
	);

	firstColumn.appendChild(nameAffinityWrapper);
	firstColumn.appendChild(competencyWrapper);
	container.appendChild(firstColumn);
	container.appendChild(controlsWrapper);

	addInfoBubble(container, job.description);

	return container;
}
