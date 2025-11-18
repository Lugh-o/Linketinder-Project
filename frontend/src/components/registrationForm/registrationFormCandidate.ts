import styles from "./registrationForm.module.css";

import { Candidate } from "../../types/Candidate";
import { COMPETENCIES, type Competency } from "../../types/Competency";
import { type FieldConfig, createLabeledInput } from "./formHelpers";
import type { AppContext } from "../../utils/AppContext";
import { Address } from "../../types/Address";

export function registrationFormCandidate(appContext: AppContext): HTMLElement {
	const form: HTMLFormElement = document.createElement("form");

	const fields: FieldConfig[] = [
		{
			label: "Nome",
			type: "text",
			name: "firstName",
			required: true,
			minLength: 3,
		},
		{
			label: "Sobrenome",
			type: "text",
			name: "lastName",
			required: true,
			minLength: 3,
		},
		{
			label: "Senha",
			type: "password",
			name: "passwd",
			required: true,
			minLength: 3,
		},
		{ label: "Email", type: "email", name: "email", required: true },
		{ label: "Estado", type: "text", name: "state", required: true },
		{ label: "País", type: "text", name: "country", required: true },
		{ label: "Cidade", type: "text", name: "city", required: true },
		{ label: "Rua", type: "text", name: "street", required: true },
		{
			label: "CEP (12345-123)",
			type: "text",
			name: "postalCode",
			required: true,
			pattern: "\\d{5}-?\\d{3}",
		},
		{
			label: "CPF (123.123.123-12)",
			type: "text",
			name: "cpf",
			required: true,
			pattern: "(\\d{3}.?){2}\\d{3}-?\\d{2}",
		},
		{
			label: "Birthday",
			type: "date",
			name: "birthday",
			max: new Date().toISOString().split("T")[0].toString(),
			required: true,
		},
		{
			label: "Formação",
			type: "text",
			name: "graduation",
			required: true,
			minLength: 5,
		},
		{
			label: "Descrição",
			type: "text",
			name: "description",
			required: true,
			minLength: 10,
		},
	];

	const inputMap: Record<string, HTMLInputElement | HTMLTextAreaElement> = {};

	fields.forEach((field: FieldConfig): void => {
		const element: HTMLDivElement = createLabeledInput(field);
		const input: HTMLInputElement | HTMLTextAreaElement =
			element.querySelector("input, textarea") as
				| HTMLInputElement
				| HTMLTextAreaElement;
		inputMap[field.name] = input;
		form.appendChild(element);
	});

	const compWrapper: HTMLDivElement =
		createCompetencyCheckboxes("competencies");
	form.appendChild(compWrapper);

	const submitButton: HTMLButtonElement = document.createElement("button");
	submitButton.type = "submit";
	submitButton.textContent = "Cadastrar Candidato";
	form.appendChild(submitButton);

	form.addEventListener("submit", (event: SubmitEvent): void => {
		event.preventDefault();

		if (!form.checkValidity()) {
			fields.forEach((field: FieldConfig): void => {
				const input = inputMap[field.name];
				const errorElement = input.nextElementSibling as HTMLElement;
				errorElement.textContent = input.validationMessage;
			});
			return;
		}

		const checkedBoxes: HTMLInputElement[] = Array.from(
			form.querySelectorAll<HTMLInputElement>(
				"input[name='competencies']:checked"
			)
		);
		const selectedCompetencies: Competency[] = checkedBoxes.map(
			(checkbox: HTMLInputElement) => checkbox.value as Competency
		);

		const compError: HTMLElement | null =
			compWrapper.querySelector(".errorMessage") ||
			compWrapper.querySelector(`.${styles.errorMessage}`);

		if (selectedCompetencies.length === 0) {
			if (compError) {
				compError.textContent = "Selecione pelo menos uma competência";
			}
			return;
		} else if (compError) {
			compError.textContent = "";
		}

		const address: Address = new Address(
			inputMap["state"].value,
			inputMap["postalCode"].value.replace("-", ""),
			inputMap["country"].value,
			inputMap["city"].value,
			inputMap["street"].value
		);

		const candidate: Candidate = new Candidate(
			0,
			0,
			inputMap["email"].value,
			inputMap["passwd"].value,
			inputMap["description"].value,
			address,
			inputMap["firstName"].value,
			inputMap["lastName"].value,
			inputMap["cpf"].value.replaceAll(".", "").replaceAll("-", ""),
			inputMap["birthday"].value,
			inputMap["graduation"].value,
			selectedCompetencies
		);

		appContext.apiGateway.createCandidate(candidate);
		appContext.router.goToCandidateDashboard(candidate, appContext);
	});
	return form;
}

function createCompetencyCheckboxes(name: string): HTMLDivElement {
	const wrapper: HTMLDivElement = document.createElement("div");
	wrapper.className = styles.formField;

	const label: HTMLSpanElement = document.createElement("span");
	label.textContent = "Competências";
	wrapper.appendChild(label);

	const competencyListWrapper: HTMLDivElement = document.createElement("div");
	competencyListWrapper.className = styles.competencyListWrapper;

	COMPETENCIES.forEach((competency: Competency) => {
		const checkboxWrapper: HTMLLabelElement =
			document.createElement("label");
		checkboxWrapper.className = styles.checkboxWrapper;

		const input: HTMLInputElement = document.createElement("input");
		input.type = "checkbox";
		input.name = name;
		input.value = competency;

		const span: HTMLSpanElement = document.createElement("span");
		span.textContent = competency.replace("_", " ");

		checkboxWrapper.appendChild(input);
		checkboxWrapper.appendChild(span);
		competencyListWrapper.appendChild(checkboxWrapper);
	});

	wrapper.appendChild(competencyListWrapper);

	const error: HTMLSpanElement = document.createElement("span");
	error.className = styles.errorMessage;
	wrapper.appendChild(error);

	return wrapper;
}
