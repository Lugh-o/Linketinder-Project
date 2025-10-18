import type { Job } from "../../types/Job";
import type { Candidate } from "../../types/Candidate";
import {
	createCommonElements,
	addInfoBubble,
	computeAffinity,
	createJobCardButton,
} from "./jobCardHelper";
import styles from "./jobCard.module.css";
import thumbsUp from "../../assets/thumbsUp.svg";
import thumbsDown from "../../assets/thumbsDown.svg";

export function candidateJobCard(
	job: Job,
	candidate: Candidate
): HTMLDivElement {
	const {
		container,
		controlsWrapper,
		firstColumn,
		nameAffinityWrapper,
		competencyWrapper,
	} = createCommonElements(job);

	createJobCardButton(controlsWrapper, thumbsUp, styles.likeButton);

	createJobCardButton(controlsWrapper, thumbsDown, styles.dislikeButton);

	const affinityContainer: HTMLDivElement = createAffinityContainer(
		candidate,
		job
	);

	nameAffinityWrapper.appendChild(affinityContainer);

	firstColumn.appendChild(nameAffinityWrapper);
	firstColumn.appendChild(competencyWrapper);
	container.appendChild(firstColumn);
	container.appendChild(controlsWrapper);

	addInfoBubble(container, job.description);

	return container;
}

function createAffinityContainer(
	candidate: Candidate,
	job: Job
): HTMLDivElement {
	const affinityContainer: HTMLDivElement = document.createElement("div");
	affinityContainer.className = styles.affinityContainer;

	const affinity = computeAffinity(candidate.competencies, job.competencies);
	const barBackground: HTMLDivElement = document.createElement("div");
	barBackground.className = styles.affinityBarBackground;

	const barFill: HTMLDivElement = document.createElement("div");
	barFill.className = styles.affinityBarFill;
	barFill.style.width = `${affinity * 100}%`;

	const label: HTMLElement = document.createElement("span");
	label.className = styles.affinityLabel;
	label.textContent = `${Math.round(affinity * 100)}%`;

	barBackground.appendChild(barFill);
	affinityContainer.appendChild(barBackground);
	affinityContainer.appendChild(label);
	return affinityContainer;
}
