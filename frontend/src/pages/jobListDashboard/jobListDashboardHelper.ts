import styles from "./jobListDashboard.module.css";

import { companyJobCard } from "../../components/jobCard/companyJobCard";
import type { Company } from "../../types/Company";
import addIcon from "../../assets/add.svg";
import { COMPETENCIES, type Competency } from "../../types/Competency";
import { Job } from "../../types/Job";
import type { AppContext } from "../../utils/AppContext";
import { Address } from "../../types/Address";

export function createNewJobButton(
	company: Company,
	appContext: AppContext
): HTMLDivElement {
	const wrapper = document.createElement("div");
	wrapper.className = styles.newJobButtonWrapper;

	const button = document.createElement("button");
	button.className = styles.createNewJobButton;

	const icon = document.createElement("img");
	icon.src = addIcon;

	const text = document.createElement("span");
	text.textContent = "Adicionar nova vaga";

	button.append(icon, text);
	button.addEventListener("click", () =>
		openCreateJobModal(company, appContext)
	);

	wrapper.appendChild(button);
	return wrapper;
}

export async function renderJobList(
	company: Company,
	appContext: AppContext
): Promise<HTMLDivElement> {
	const container: HTMLDivElement = document.createElement("div");
	container.className = styles.jobListContainer;

	const jobs: Job[] = await appContext.apiGateway.getAllJobsFromCompany(
		company.idCompany
	);

	jobs.forEach((job: Job) => {
		const jobCard: HTMLDivElement = companyJobCard(job, () => {
			appContext.router.goToJobList(company, appContext);
		});
		container.appendChild(jobCard);
	});

	return container;
}

export function openCreateJobModal(
	company: Company,
	appContext: AppContext
): void {
	const overlay = document.createElement("div");
	overlay.className = styles.createJobModalOverlay;

	const modal = document.createElement("div");
	modal.className = styles.createJobModal;

	const title = document.createElement("h2");
	title.className = styles.createJobTitle;
	title.textContent = "Criar vaga";

	const form = buildJobCreationForm(company, overlay, appContext);

	modal.append(title, form);
	overlay.appendChild(modal);
	document.body.appendChild(overlay);
}

export function buildJobCreationForm(
	company: Company,
	overlay: HTMLDivElement,
	appContext: AppContext
): HTMLFormElement {
	const form = document.createElement("form");
	form.className = styles.createJobForm;

	form.append(
		createLabeledInput(
			"Nome:",
			"text",
			styles.jobNameInput,
			"jobNameInput"
		),
		createLabeledTextarea(
			"Descrição:",
			styles.jobDescriptionInput,
			"jobDescriptionInput"
		),
		createLabeledInput(
			"Estado:",
			"text",
			styles.jobNameInput,
			"jobStateInput"
		),
		createLabeledInput(
			"Cidade:",
			"text",
			styles.jobNameInput,
			"jobCityInput"
		),
		createLabeledInput(
			"Rua:",
			"text",
			styles.jobNameInput,
			"jobStreetInput"
		),
		createLabeledInput(
			"País:",
			"text",
			styles.jobNameInput,
			"jobCountrytInput"
		),
		createLabeledInput("CEP:", "text", styles.jobNameInput, "jobZipInput"),
		createCompetencySelector(),
		createFormButtons(company, overlay, form, appContext)
	);

	return form;
}

export function createLabeledInput(
	labelText: string,
	type: string,
	className: string,
	inputId: string
): HTMLElement {
	const wrapper = document.createElement("div");
	wrapper.className = styles.formField;

	const label = document.createElement("label");
	label.htmlFor = inputId;
	label.textContent = labelText;

	const input = document.createElement("input");
	input.type = type;
	input.id = inputId;
	input.className = className;
	input.required = true;

	wrapper.append(label, input);
	return wrapper;
}

export function createLabeledTextarea(
	labelText: string,
	className: string,
	textareaId: string
): HTMLElement {
	const wrapper = document.createElement("div");
	wrapper.className = styles.formField;

	const label = document.createElement("label");
	label.htmlFor = textareaId;
	label.textContent = labelText;

	const textarea = document.createElement("textarea");
	textarea.id = textareaId;
	textarea.className = className;
	textarea.required = true;

	wrapper.append(label, textarea);
	return wrapper;
}

export function createCompetencySelector(): HTMLElement {
	const wrapper = document.createElement("div");
	wrapper.className = styles.formField;

	const label = document.createElement("label");
	label.textContent = "Competências:";
	wrapper.appendChild(label);

	const listWrapper = document.createElement("div");
	listWrapper.className = styles.competencyListWrapper;

	COMPETENCIES.forEach((comp) => {
		const option = document.createElement("label");
		option.className = styles.competencyCheck;

		const checkbox = document.createElement("input");
		checkbox.type = "checkbox";
		checkbox.name = "competencies";
		checkbox.value = comp;

		option.append(
			checkbox,
			document.createTextNode(comp.replace("_", " "))
		);
		listWrapper.appendChild(option);
	});

	wrapper.appendChild(listWrapper);
	return wrapper;
}

export function createFormButtons(
	company: Company,
	overlay: HTMLDivElement,
	form: HTMLFormElement,
	appContext: AppContext
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
		handleJobFormSubmit(e, company, form, overlay, appContext)
	);

	wrapper.append(submitBtn, cancelBtn);
	return wrapper;
}

export function handleJobFormSubmit(
	e: Event,
	company: Company,
	form: HTMLFormElement,
	overlay: HTMLDivElement,
	appContext: AppContext
): void {
	e.preventDefault();

	const nameInput = form.querySelector<HTMLInputElement>("#jobNameInput");
	const descriptionInput = form.querySelector<HTMLTextAreaElement>(
		"#jobDescriptionInput"
	);
	const countryInput =
		form.querySelector<HTMLTextAreaElement>("#jobCountryInput");
	const stateInput =
		form.querySelector<HTMLTextAreaElement>("#jobStateInput");
	const cityInput = form.querySelector<HTMLTextAreaElement>("#jobCityInput");
	const streetInput =
		form.querySelector<HTMLTextAreaElement>("#jobStreetInput");
	const zipInput = form.querySelector<HTMLTextAreaElement>("#jobZipInput");
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

	const address: Address = new Address(
		stateInput?.value.trim() ?? "",
		zipInput?.value.trim() ?? "",
		countryInput?.value.trim() ?? "",
		cityInput?.value.trim() ?? "",
		streetInput?.value.trim() ?? ""
	);

	const newJob = new Job(
		0,
		company.idCompany,
		nameInput.value.trim(),
		descriptionInput.value.trim(),
		selectedCompetencies,
		address
	);

	appContext.apiGateway.createJob(newJob);
	overlay.remove();

	appContext.router.goToCompanyDashboard(company, appContext);
}
