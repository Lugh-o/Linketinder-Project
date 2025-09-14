import styles from "./candidateDashboard.module.css";

import { store } from "../../Store";
import type { Candidate } from "../../types/Candidate";
import type { Job } from "../../types/Job";
import { jobCard } from "../../components/jobCard/jobCard";
import { header } from "../../components/header/header";

export function candidateDashboard(candidate: Candidate): HTMLDivElement {
	const container: HTMLDivElement = document.createElement("div");

	const headerElement: HTMLElement = header(candidate);
	container.appendChild(headerElement);

	const jobListContainer: HTMLDivElement = document.createElement("div");
	jobListContainer.className = styles.jobListContainer;

	const jobList: Job[] = store.getJobList();

	jobList.forEach((job) => {
		const card = jobCard(job, candidate);
		jobListContainer.appendChild(card);
	});

	container.appendChild(jobListContainer);
	return container;
}
