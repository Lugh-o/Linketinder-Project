import styles from "./jobListDashboard.module.css";

import { companyJobCard } from "../../components/jobCard/companyJobCard";
import type { Company } from "../../types/Company";
import addIcon from "../../assets/add.svg";
import { COMPETENCIES, type Competency } from "../../types/Competency";
import { store } from "../../Store";
import { Job } from "../../types/Job";
import { navigateTo } from "../../utils/router";
import { jobListDashboard } from "./jobListDashboard";

export function createNewJobButton(company: Company): HTMLDivElement {
	const wrapper = document.createElement("div");
	wrapper.className = styles.newJobButtonWrapper;

	const button = document.createElement("button");
	button.className = styles.createNewJobButton;

	const icon = document.createElement("img");
	icon.src = addIcon;
	icon.alt = "Adicionar nova vaga";

	const text = document.createElement("span");
	text.textContent = "Criar vaga de emprego";

	button.append(icon, text);
	button.addEventListener("click", () => openCreateJobModal(company));

	wrapper.appendChild(button);
	return wrapper;
}

export function renderJobList(company: Company): HTMLDivElement {
	const jobListContainer = document.createElement("div");
	jobListContainer.className = styles.jobListContainer;

	company.getJobList().forEach((job) => {
		const jobCardElement = companyJobCard(job, company);
		jobListContainer.appendChild(jobCardElement);
	});

	return jobListContainer;
}

export function openCreateJobModal(company: Company): void {
	const overlay = document.createElement("div");
	overlay.className = styles.createJobModalOverlay;

	const modal = document.createElement("div");
	modal.className = styles.createJobModal;

	const title = document.createElement("h2");
	title.textContent = "Criar vaga";
	title.className = styles.createJobTitle;

	const form = buildJobCreationForm(company, overlay);

	modal.append(title, form);
	overlay.appendChild(modal);
	document.body.appendChild(overlay);
}

export function buildJobCreationForm(
	company: Company,
	overlay: HTMLDivElement
): HTMLFormElement {
	const form = document.createElement("form");

	form.append(
		createLabeledInput("Nome da vaga:", "text", styles.jobNameInput, true),
		createLabeledTextarea(
			"Descrição da vaga:",
			styles.jobDescriptionInput,
			true
		),
		createCompetencySelector(),
		createFormButtons(company, overlay, form)
	);

	return form;
}

export function createLabeledInput(
	labelText: string,
	type: string,
	className: string,
	required = false
): HTMLElement {
	const wrapper = document.createElement("div");

	const label = document.createElement("label");
	label.textContent = labelText;

	const input = document.createElement("input");
	input.type = type;
	input.className = className;
	input.required = required;

	wrapper.append(label, input);
	return wrapper;
}

export function createLabeledTextarea(
	labelText: string,
	className: string,
	required = false
): HTMLElement {
	const wrapper = document.createElement("div");

	const label = document.createElement("label");
	label.textContent = labelText;

	const textarea = document.createElement("textarea");
	textarea.className = className;
	textarea.required = required;

	wrapper.append(label, textarea);
	return wrapper;
}

export function createCompetencySelector(): HTMLElement {
	const section = document.createElement("div");

	const label = document.createElement("label");
	label.textContent = "Competências:";
	section.appendChild(label);

	const wrapper = document.createElement("div");
	wrapper.className = styles.competencyListWrapper;

	COMPETENCIES.forEach((comp) => {
		const option = document.createElement("label");
		option.className = styles.competencyCheck;

		const checkbox = document.createElement("input");
		checkbox.type = "checkbox";
		checkbox.name = "competencies";
		checkbox.value = comp;

		option.append(checkbox, document.createTextNode(comp));
		wrapper.appendChild(option);
	});

	section.appendChild(wrapper);
	return section;
}

export function createFormButtons(
	company: Company,
	overlay: HTMLDivElement,
	form: HTMLFormElement
): HTMLElement {
	const wrapper = document.createElement("div");
	wrapper.className = styles.modalButtonWrapper;

	const submitBtn = document.createElement("button");
	submitBtn.type = "submit";
	submitBtn.textContent = "Criar";

	const cancelBtn = document.createElement("button");
	cancelBtn.type = "button";
	cancelBtn.textContent = "Cancelar";
	cancelBtn.addEventListener("click", () => overlay.remove());

	form.addEventListener("submit", (e) =>
		handleJobFormSubmit(e, company, form, overlay)
	);

	wrapper.append(submitBtn, cancelBtn);
	return wrapper;
}

export function handleJobFormSubmit(
	e: Event,
	company: Company,
	form: HTMLFormElement,
	overlay: HTMLDivElement
): void {
	e.preventDefault();

	const nameInput =
		form.querySelector<HTMLInputElement>("input[type='text']");
	const descriptionInput =
		form.querySelector<HTMLTextAreaElement>("textarea");
	const selectedCompetencies = Array.from(
		form.querySelectorAll<HTMLInputElement>(
			"input[name='competencies']:checked"
		)
	).map((el) => el.value as Competency);

	if (!nameInput?.value.trim() || !descriptionInput?.value.trim()) {
		alert("Preencha todos os campos obrigatórios.");
		return;
	}

	if (selectedCompetencies.length === 0) {
		alert("Selecione pelo menos uma competência.");
		return;
	}

	const newJob = new Job(
		store.getNextJobId(),
		nameInput.value.trim(),
		descriptionInput.value.trim(),
		selectedCompetencies
	);

	company.addJob(newJob);
	store.save();
	overlay.remove();
	navigateTo(jobListDashboard(company));
}
