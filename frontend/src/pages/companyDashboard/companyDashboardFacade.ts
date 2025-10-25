import styles from "./companyDashboard.module.css";
import { Chart } from "chart.js/auto";
import { candidateCard } from "../../components/candidateCard/candidateCard";
import type { Company } from "../../types/Company";
import { COMPETENCIES, type Competency } from "../../types/Competency";
import { companyHeader } from "../../components/header/companyHeader";
import type { AppContext } from "../../utils/AppContext";

export class CompanyDashboardFacade {
	private appContext: AppContext;

	constructor(appContext: AppContext) {
		this.appContext = appContext;
	}

	render(company: Company): HTMLDivElement {
		const container: HTMLDivElement = document.createElement("div");
		container.className = styles.companyDashboardContainer;

		const headerElement: HTMLElement = companyHeader(
			company,
			this.appContext
		);
		container.appendChild(headerElement);

		const scrollableContainer: HTMLDivElement =
			document.createElement("div");
		scrollableContainer.className = styles.scrollableContainer;

		const candidateListContainer = this.createCandidateListContainer();
		const chartContainer = this.createCompetencyChart(
			candidateListContainer.data
		);

		scrollableContainer.appendChild(chartContainer);
		scrollableContainer.appendChild(candidateListContainer.element);

		container.appendChild(scrollableContainer);
		return container;
	}

	private createCandidateListContainer(): {
		element: HTMLDivElement;
		data: Record<Competency, number>;
	} {
		const candidateListContainer: HTMLDivElement =
			document.createElement("div");
		candidateListContainer.className = styles.candidateListContainer;

		const data: Record<Competency, number> = {};
		COMPETENCIES.forEach((c) => (data[c] = 0));

		this.appContext.store.getCandidateList().forEach((candidate) => {
			const card: HTMLDivElement = candidateCard(candidate);
			candidateListContainer.appendChild(card);

			candidate.competencies.forEach((comp) => {
				data[comp] += 1;
			});
		});

		return { element: candidateListContainer, data };
	}

	private createCompetencyChart(
		data: Record<Competency, number>
	): HTMLDivElement {
		const canvaContainer: HTMLDivElement = document.createElement("div");
		canvaContainer.className = styles.canvaContainer;

		const entries = Object.entries(data).filter(([_, count]) => count > 0);
		entries.sort((a, b) => b[1] - a[1]);

		const labels: string[] = entries.map(([label]) => label);
		const values: number[] = entries.map(([_, count]) => count);

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
					x: {
						ticks: { display: false },
						grid: { drawTicks: false },
					},
					y: { ticks: { display: true }, grid: { drawTicks: false } },
				},
			},
		});

		canvaContainer.appendChild(canvas);
		return canvaContainer;
	}
}
