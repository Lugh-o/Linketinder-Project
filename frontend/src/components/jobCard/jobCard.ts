import styles from "./jobCard.module.css";
import type { Job } from "../../types/Job";
import { competencyBubble } from "../competencyBubble/competencyBubble";

export function createBubble(container: HTMLDivElement, job: Job): void {
	const infoBubble: HTMLDivElement = document.createElement("div");
	infoBubble.className = styles.jobInfo;
	infoBubble.textContent = job.description;
	container.appendChild(infoBubble);

	container.addEventListener("mouseenter", () => {
		const rect: DOMRect = container.getBoundingClientRect();
		const bubbleWidth: number = 300;
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

export function createCommonElements(job: Job): HTMLDivElement[] {
	const container: HTMLDivElement = document.createElement("div");
	container.className = styles.container;

	const firstColumn: HTMLDivElement = document.createElement("div");
	firstColumn.className = styles.firstColumn;

	const nameAffinityWrapper: HTMLDivElement = document.createElement("div");

	const jobName: HTMLHeadingElement = document.createElement("h2");
	jobName.textContent = job.name;
	nameAffinityWrapper.appendChild(jobName);

	const competencyWrapper: HTMLDivElement = document.createElement("div");
	job.competencies.forEach((competency) => {
		const bubble: HTMLDivElement = competencyBubble(competency);
		competencyWrapper.appendChild(bubble);
	});
	competencyWrapper.className = styles.competencyWrapper;

	const controlsWrapper: HTMLDivElement = document.createElement("div");
	controlsWrapper.className = styles.controlsWrapper;

	return [
		container,
		controlsWrapper,
		firstColumn,
		nameAffinityWrapper,
		competencyWrapper,
	];
}
