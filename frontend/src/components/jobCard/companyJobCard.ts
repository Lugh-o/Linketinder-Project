import styles from "./jobCard.module.css";
import type { Job } from "../../types/Job";
import deleteIcon from "../../assets/delete.svg";
import type { Company } from "../../types/Company";
import { navigateTo } from "../../utils/router";
import { jobListDashboard } from "../../pages/jobListDashboard/jobListDashboard";
import { store } from "../../Store";
import { createBubble, createCommonElements } from "./jobCard";

export function companyJobCard(job: Job, company: Company): HTMLDivElement {
	const commonElements: HTMLDivElement[] = createCommonElements(job);
	const container: HTMLDivElement = commonElements[0];
	const controlsWrapper: HTMLDivElement = commonElements[1];
	const firstColumn: HTMLDivElement = commonElements[2];
	const nameAffinityWrapper: HTMLDivElement = commonElements[3];
	const competencyWrapper: HTMLDivElement = commonElements[4];

	const deleteButton: HTMLButtonElement = document.createElement("button");
	const deleteImage: HTMLImageElement = document.createElement("img");
	deleteImage.src = deleteIcon;
	deleteButton.appendChild(deleteImage);
	deleteButton.classList.add(styles.button, styles.dislikeButton);
	deleteButton.addEventListener("click", () => {
		let index = company.getJobList().indexOf(job);
		company.removeJobByIndex(index);
		store.save(); // Evitar dependencias circulares
		navigateTo(jobListDashboard(company));
	});

	controlsWrapper.appendChild(deleteButton);
	firstColumn.appendChild(nameAffinityWrapper);
	firstColumn.appendChild(competencyWrapper);
	container.appendChild(firstColumn);
	container.appendChild(controlsWrapper);

	createBubble(container, job);

	return container;
}
