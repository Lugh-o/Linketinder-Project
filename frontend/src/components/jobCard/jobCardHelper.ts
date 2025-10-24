import styles from "./jobCard.module.css";

import { Job } from "../../types/Job";
import { competencyBubble } from "../competencyBubble/competencyBubble";

export function createCommonElements(job: Job): {
	container: HTMLDivElement;
	controlsWrapper: HTMLDivElement;
	firstColumn: HTMLDivElement;
	nameAffinityWrapper: HTMLDivElement;
	competencyWrapper: HTMLDivElement;
} {
	const container: HTMLDivElement = document.createElement("div");
	container.className = styles.container;

	const firstColumn: HTMLDivElement = document.createElement("div");
	firstColumn.className = styles.firstColumn;

	const nameAffinityWrapper: HTMLDivElement = document.createElement("div");

	const jobName: HTMLHeadingElement = document.createElement("h2");
	jobName.textContent = job.name;
	nameAffinityWrapper.appendChild(jobName);

	const competencyWrapper: HTMLDivElement = document.createElement("div");
	competencyWrapper.className = styles.competencyWrapper;
	job.competencies.forEach((competency) => {
		const bubble = competencyBubble(competency);
		competencyWrapper.appendChild(bubble);
	});

	const controlsWrapper: HTMLDivElement = document.createElement("div");
	controlsWrapper.className = styles.controlsWrapper;

	return {
		container,
		controlsWrapper,
		firstColumn,
		nameAffinityWrapper,
		competencyWrapper,
	};
}

export function addInfoBubble(
	container: HTMLDivElement,
	description: string,
	bubbleWidth = 300
) {
	const infoBubble: HTMLDivElement = document.createElement("div");
	infoBubble.className = styles.jobInfo;
	infoBubble.textContent = description;
	container.appendChild(infoBubble);

	container.addEventListener("mouseenter", () => {
		const rect = container.getBoundingClientRect();
		if (rect.right + bubbleWidth > window.innerWidth) {
			infoBubble.style.left = "auto";
			infoBubble.style.right = "100%";
			infoBubble.style.marginLeft = "0";
			infoBubble.style.marginRight = "10px";
		} else {
			infoBubble.style.left = "100%";
			infoBubble.style.right = "auto";
			infoBubble.style.marginLeft = "10px";
			infoBubble.style.marginRight = "0";
		}
	});
}

export function computeAffinity(
	candidateCompetencies: string[],
	jobCompetencies: string[]
): number {
	if (!jobCompetencies.length) return 0;
	const matching = jobCompetencies.filter((comp) =>
		candidateCompetencies.includes(comp)
	);
	return matching.length / jobCompetencies.length;
}

export function createJobCardButton(
	appendTo: HTMLElement,
	iconImage: string,
	style: string,
	onClick?: () => void
) {
	const button: HTMLButtonElement = document.createElement("button");
	const icon: HTMLImageElement = document.createElement("img");
	icon.src = iconImage;
	button.appendChild(icon);
	button.classList.add(styles.button, style);

	if (onClick) {
		button.addEventListener("click", onClick);
	}

	appendTo.appendChild(button);
}
