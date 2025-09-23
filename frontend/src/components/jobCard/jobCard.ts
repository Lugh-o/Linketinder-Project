import styles from "./jobCard.module.css";
import type { Job } from "../../types/Job";
import thumbsUp from "../../assets/thumbsUp.svg";
import thumbsDown from "../../assets/thumbsDown.svg";
import deleteIcon from "../../assets/delete.svg";
import { competencyBubble } from "../competencyBubble/competencyBubble";
import type { Person } from "../../types/Person";
import { Candidate } from "../../types/Candidate";
import type { Company } from "../../types/Company";
import { navigateTo } from "../../utils/router";
import { jobListDashboard } from "../../pages/jobListDashboard/jobListDashboard";
import { store } from "../../Store";

export function jobCard(job: Job, person: Person): HTMLDivElement {
	const container: HTMLDivElement = document.createElement("div");
	container.className = styles.container;

	const firstColumn: HTMLDivElement = document.createElement("div");
	firstColumn.className = styles.firstColumn;

	const nameAfinittyWrapper: HTMLDivElement = document.createElement("div");

	const jobName: HTMLHeadingElement = document.createElement("h2");
	jobName.textContent = job.name;
	nameAfinittyWrapper.appendChild(jobName);

	const competencyWrapper: HTMLDivElement = document.createElement("div");
	job.competencies.forEach((competency) => {
		const bubble: HTMLDivElement = competencyBubble(competency);
		competencyWrapper.appendChild(bubble);
	});
	competencyWrapper.className = styles.competencyWrapper;

	const controlsWrapper: HTMLDivElement = document.createElement("div");
	controlsWrapper.className = styles.controlsWrapper;

	if (person instanceof Candidate) {
		const likeButton: HTMLButtonElement = document.createElement("button");
		const likeImage: HTMLImageElement = document.createElement("img");
		likeImage.src = thumbsUp;
		likeButton.appendChild(likeImage);
		likeButton.classList.add(styles.button, styles.likeButton);

		const dislikeButton: HTMLButtonElement =
			document.createElement("button");
		const dislikeImage: HTMLImageElement = document.createElement("img");
		dislikeImage.src = thumbsDown;
		dislikeButton.appendChild(dislikeImage);
		dislikeButton.classList.add(styles.button, styles.dislikeButton);

		controlsWrapper.appendChild(likeButton);
		controlsWrapper.appendChild(dislikeButton);

		let selfCompetencies = (person as Candidate).competencies;
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

		const affinityBarBackground: HTMLDivElement =
			document.createElement("div");
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
		nameAfinittyWrapper.appendChild(affinityIndexContainer);
	} else {
		const deleteButton: HTMLButtonElement =
			document.createElement("button");
		const deleteImage: HTMLImageElement = document.createElement("img");
		deleteImage.src = deleteIcon;
		deleteButton.appendChild(deleteImage);
		deleteButton.classList.add(styles.button, styles.dislikeButton);
		deleteButton.addEventListener("click", () => {
			let company = person as Company;
			let index = company.getJobList().indexOf(job);
			company.removeJobByIndex(index);
			store.save(); // Evitar dependencias circulares
			navigateTo(jobListDashboard(company));
		});
		controlsWrapper.appendChild(deleteButton);
	}

	firstColumn.appendChild(nameAfinittyWrapper);
	firstColumn.appendChild(competencyWrapper);
	container.appendChild(firstColumn);

	container.appendChild(controlsWrapper);

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

	return container;
}
