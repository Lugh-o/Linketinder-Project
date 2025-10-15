import styles from "./candidateDashboard.module.css";

import { store } from "../../Store";
import type { Candidate } from "../../types/Candidate";
import type { Job } from "../../types/Job";
import { candidateJobCard } from "../../components/jobCard/candidateJobCard";
import { candidateHeader } from "../../components/header/candidateHeader";

export function candidateDashboard(candidate: Candidate): HTMLDivElement {
	const container: HTMLDivElement = document.createElement("div");

	const headerElement: HTMLElement = candidateHeader(candidate);
	container.appendChild(headerElement);

	const jobListContainer: HTMLDivElement = document.createElement("div");
	jobListContainer.className = styles.jobListContainer;

	const jobList: Job[] = store.getJobList();

	jobList.forEach((job) => {
		const card = candidateJobCard(job, candidate);
		jobListContainer.appendChild(card);
	});

	container.appendChild(jobListContainer);
	return container;
}
