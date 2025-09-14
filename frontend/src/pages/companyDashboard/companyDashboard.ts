import styles from "./companyDashboard.module.css";

import { Chart } from "chart.js/auto";

import { candidateCard } from "../../components/candidateCard/candidateCard";
import { header } from "../../components/header/header";
import { store } from "../../Store";
import type { Company } from "../../types/Company";
import { COMPETENCIES, type Competency } from "../../types/Competency";

export function companyDashboard(company: Company): HTMLDivElement {
	const container: HTMLDivElement = document.createElement("div");
	container.className = styles.companyDashboardContainer;
	const headerElement: HTMLElement = header(company);
	container.appendChild(headerElement);

	let data: Record<Competency, number> = {};
	COMPETENCIES.forEach((comp) => {
		data[comp] = 0;
	});

	const scrollableContainer: HTMLDivElement = document.createElement("div");
	scrollableContainer.className = styles.scrollableContainer;

	const candidateListContainer: HTMLDivElement =
		document.createElement("div");
	candidateListContainer.className = styles.candidateListContainer;

	store.getCandidateList().forEach((candidate) => {
		const card: HTMLDivElement = candidateCard(candidate);
		candidateListContainer.appendChild(card);
		candidate.competencies.forEach((comp) => {
			data[comp] += 1;
		});
	});

	const entries = Object.entries(data).filter(([_, count]) => count > 0);
	entries.sort((a, b) => b[1] - a[1]);

	const labels: string[] = entries.map(([label]) => label);
	const values: number[] = entries.map(([_, count]) => count);

	const canvaContainer: HTMLDivElement = document.createElement("div");
	canvaContainer.className = styles.canvaContainer;

	const canvas: HTMLCanvasElement = document.createElement("canvas");
	new Chart(canvas, {
		type: "bar",
		data: {
			labels,
			datasets: [
				{
					data: values,
					backgroundColor: "#007bff",
					borderWidth: 0,
					barThickness: 15,
					maxBarThickness: 20,
				},
			],
		},
		options: {
			responsive: true,
			maintainAspectRatio: false,
			indexAxis: "y",
			plugins: {
				legend: { display: false },
				tooltip: { enabled: true },
			},
			scales: {
				x: { ticks: { display: false }, grid: { drawTicks: false } },
				y: { ticks: { display: true }, grid: { drawTicks: false } },
			},
		},
	});
	canvaContainer.appendChild(canvas);
	scrollableContainer.appendChild(canvaContainer);

	scrollableContainer.appendChild(candidateListContainer);
	container.appendChild(scrollableContainer);
	return container;
}
