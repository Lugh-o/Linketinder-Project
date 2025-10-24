import styles from "./candidateDashboard.module.css";

import type { Candidate } from "../../types/Candidate";
import type { Job } from "../../types/Job";
import { candidateJobCard } from "../../components/jobCard/candidateJobCard";
import { candidateHeader } from "../../components/header/candidateHeader";
import type { AppContext } from "../../utils/AppContext";

export function candidateDashboard(
	candidate: Candidate,
	appContext: AppContext
): HTMLDivElement {
	const container: HTMLDivElement = document.createElement("div");

	const headerElement: HTMLElement = candidateHeader(candidate, appContext);
	container.appendChild(headerElement);

	const jobListContainer: HTMLDivElement = document.createElement("div");
	jobListContainer.className = styles.jobListContainer;

	const jobList: Job[] = appContext.store.getJobList();

	jobList.forEach((job) => {
		const card = candidateJobCard(job, candidate);
		jobListContainer.appendChild(card);
	});

	container.appendChild(jobListContainer);
	return container;
}
