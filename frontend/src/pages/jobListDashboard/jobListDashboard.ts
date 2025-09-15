import styles from "./jobListDashboard.module.css";

import { header } from "../../components/header/header";
import { jobCard } from "../../components/jobCard/jobCard";
import type { Company } from "../../types/Company";
import addIcon from "../../assets/add.svg";
import { COMPETENCIES, type Competency } from "../../types/Competency";
import { store } from "../../Store";
import { Job } from "../../types/Job";
import { navigateTo } from "../../utils/router";

export function jobListDashboard(company: Company): HTMLDivElement {
	const container: HTMLDivElement = document.createElement("div");
	const headerElement: HTMLElement = header(company);
	container.appendChild(headerElement);

	const jobListContainer: HTMLDivElement = document.createElement("div");
	jobListContainer.className = styles.jobListContainer;

	const newJobButtonWrapper: HTMLDivElement = document.createElement("div");
	newJobButtonWrapper.className = styles.newJobButtonWrapper;

	const createNewJobButton: HTMLButtonElement =
		document.createElement("button");
	createNewJobButton.className = styles.createNewJobButton;
	const newJobImage: HTMLImageElement = document.createElement("img");
	newJobImage.src = addIcon;
	createNewJobButton.appendChild(newJobImage);

	const createNewJobText: HTMLSpanElement = document.createElement("span");
	createNewJobText.textContent = "Criar vaga de emprego";
	createNewJobButton.appendChild(createNewJobText);
	createNewJobButton.addEventListener("click", () => {
		const createJobModalOverlay: HTMLDivElement =
			document.createElement("div");
		createJobModalOverlay.className = styles.createJobModalOverlay;
		const createJobModal: HTMLDivElement = document.createElement("div");
		createJobModal.className = styles.createJobModal;

		const createJobTitle: HTMLHeadingElement = document.createElement("h2");
		createJobTitle.textContent = "Criar vaga";
		createJobTitle.className = styles.createJobTitle;
		createJobModal.appendChild(createJobTitle);
		const form: HTMLFormElement = document.createElement("form");

		const nameLabel: HTMLLabelElement = document.createElement("label");
		nameLabel.textContent = "Nome da vaga:";
		const nameInput: HTMLInputElement = document.createElement("input");
		nameInput.className = styles.jobNameInput;
		nameInput.type = "text";
		nameInput.required = true;
		nameInput.minLength = 3;

		form.appendChild(nameLabel);
		form.appendChild(nameInput);

		const descriptionLabel: HTMLLabelElement =
			document.createElement("label");
		descriptionLabel.textContent = "Descrição da vaga:";
		const descriptionInput: HTMLTextAreaElement =
			document.createElement("textarea");
		descriptionInput.className = styles.jobNameInput;
		descriptionInput.required = true;
		descriptionInput.minLength = 3;

		form.appendChild(descriptionLabel);
		form.appendChild(descriptionInput);

		const compLabel: HTMLLabelElement = document.createElement("label");
		compLabel.textContent = "Competências:";
		form.appendChild(compLabel);

		const competenciesWrapper: HTMLDivElement =
			document.createElement("div");
		competenciesWrapper.className = styles.competencyListWrapper;

		COMPETENCIES.forEach((comp) => {
			const wrapper: HTMLLabelElement = document.createElement("label");
			wrapper.className = styles.competencyCheck;
			const checkbox: HTMLInputElement = document.createElement("input");
			checkbox.type = "checkbox";
			checkbox.value = comp;
			checkbox.name = "competencies";
			wrapper.appendChild(checkbox);
			wrapper.appendChild(document.createTextNode(comp));
			competenciesWrapper.appendChild(wrapper);
		});

		form.appendChild(competenciesWrapper);

		const submitBtn: HTMLButtonElement = document.createElement("button");
		submitBtn.type = "submit";
		submitBtn.textContent = "Criar";
		form.appendChild(submitBtn);

		const closeBtn: HTMLButtonElement = document.createElement("button");
		closeBtn.type = "button";
		closeBtn.textContent = "Cancelar";
		closeBtn.addEventListener("click", () => {
			document.body.removeChild(createJobModalOverlay);
		});
		form.appendChild(closeBtn);

		form.addEventListener("submit", (e) => {
			e.preventDefault();
			const name: string = nameInput.value.trim();
			const description: string = descriptionInput.value.trim();
			const selectedCompetencies: Competency[] = Array.from(
				form.querySelectorAll<HTMLInputElement>(
					"input[name='competencies']:checked"
				)
			).map((el) => el.value);

			if (name && selectedCompetencies.length > 0) {
				const newJob = new Job(
					store.getNextJobId(),
					name,
					description,
					selectedCompetencies
				);
				company.addJob(newJob);
				store.save(); // Evitar dependencias circulares

				document.body.removeChild(createJobModalOverlay);
				navigateTo(jobListDashboard(company));
			}
		});

		createJobModal.appendChild(form);
		createJobModalOverlay.appendChild(createJobModal);
		document.body.appendChild(createJobModalOverlay);
	});

	newJobButtonWrapper.appendChild(createNewJobButton);
	container.appendChild(newJobButtonWrapper);
	company.getJobList().forEach((job) => {
		const jobCardElement = jobCard(job, company);
		jobListContainer.appendChild(jobCardElement);
	});

	container.appendChild(jobListContainer);

	return container;
}
