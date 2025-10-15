import styles from "./jobCard.module.css";
import type { Job } from "../../types/Job";
import thumbsUp from "../../assets/thumbsUp.svg";
import thumbsDown from "../../assets/thumbsDown.svg";
import { Candidate } from "../../types/Candidate";
import { createBubble, createCommonElements } from "./jobCard";

export function candidateJobCard(
	job: Job,
	candidate: Candidate
): HTMLDivElement {
	const commonElements: HTMLDivElement[] = createCommonElements(job);
	const container: HTMLDivElement = commonElements[0];
	const controlsWrapper: HTMLDivElement = commonElements[1];
	const firstColumn: HTMLDivElement = commonElements[2];
	const nameAffinityWrapper: HTMLDivElement = commonElements[3];
	const competencyWrapper: HTMLDivElement = commonElements[4];

	const likeButton: HTMLButtonElement = document.createElement("button");
	const likeImage: HTMLImageElement = document.createElement("img");
	likeImage.src = thumbsUp;
	likeButton.appendChild(likeImage);
	likeButton.classList.add(styles.button, styles.likeButton);

	const dislikeButton: HTMLButtonElement = document.createElement("button");
	const dislikeImage: HTMLImageElement = document.createElement("img");
	dislikeImage.src = thumbsDown;
	dislikeButton.appendChild(dislikeImage);
	dislikeButton.classList.add(styles.button, styles.dislikeButton);

	controlsWrapper.appendChild(likeButton);
	controlsWrapper.appendChild(dislikeButton);

	let selfCompetencies = candidate.competencies;
	let jobCompetencies = job.competencies;

	let competencyCount = jobCompetencies.length;
	let matchingCompetencyCount = 0;

	jobCompetencies.forEach((comp) => {
		if (selfCompetencies.includes(comp)) {
			matchingCompetencyCount++;
		}
	});

	let affinityIndex = matchingCompetencyCount / competencyCount;

	const affinityIndexContainer: HTMLDivElement =
		document.createElement("div");
	affinityIndexContainer.className = styles.affinityContainer;

	const affinityBarBackground: HTMLDivElement = document.createElement("div");
	affinityBarBackground.className = styles.affinityBarBackground;

	const affinityBarFill: HTMLDivElement = document.createElement("div");
	affinityBarFill.className = styles.affinityBarFill;
	affinityBarFill.style.width = `${affinityIndex * 100}%`;

	const affinityLabel: HTMLElement = document.createElement("span");
	affinityLabel.className = styles.affinityLabel;
	affinityLabel.textContent = `${Math.round(affinityIndex * 100)}%`;

	affinityBarBackground.appendChild(affinityBarFill);
	affinityIndexContainer.appendChild(affinityBarBackground);
	affinityIndexContainer.appendChild(affinityLabel);
	nameAffinityWrapper.appendChild(affinityIndexContainer);
	firstColumn.appendChild(nameAffinityWrapper);
	firstColumn.appendChild(competencyWrapper);
	container.appendChild(firstColumn);
	container.appendChild(controlsWrapper);

	createBubble(container, job);

	return container;
}
