import styles from "./candidateCard.module.css";

import type { Candidate } from "../../types/Candidate";
import { competencyBubble } from "../competencyBubble/competencyBubble";
import thumbsUp from "../../assets/thumbsUp.svg";
import thumbsDown from "../../assets/thumbsDown.svg";

export function candidateCard(candidate: Candidate): HTMLDivElement {
	const container: HTMLDivElement = document.createElement("div");
	container.className = styles.candidateContainer;

	const title: HTMLHeadingElement = document.createElement("h1");
	title.textContent = `${candidate.graduation}`;
	container.appendChild(title);

	attachCompetencyContainer(container, candidate);
	attachControls(container);
	attachInfoBubble(container, candidate);

	return container;
}

function attachCompetencyContainer(
	container: HTMLDivElement,
	candidate: Candidate
) {
	const competencyContainer: HTMLDivElement = document.createElement("div");
	competencyContainer.className = styles.competencyWrapper;
	candidate.competencies.forEach((competency) => {
		const bubble = competencyBubble(competency);
		competencyContainer.appendChild(bubble);
	});
	container.appendChild(competencyContainer);
}

function attachControls(container: HTMLDivElement) {
	const controlsWrapper: HTMLDivElement = document.createElement("div");
	controlsWrapper.className = styles.companyControlsWrapper;
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
	container.appendChild(controlsWrapper);
}

function attachInfoBubble(container: HTMLDivElement, candidate: Candidate) {
	const infoBubble: HTMLDivElement = document.createElement("div");
	infoBubble.className = styles.candidateInfo;
	infoBubble.textContent = `${candidate.description}`;

	container.addEventListener("mouseenter", () => {
		const rect: DOMRect = container.getBoundingClientRect();
		const bubbleWidth: number = 200;
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
	container.appendChild(infoBubble);
}
