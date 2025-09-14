import { Company } from "../../types/Company";
import { store } from "../../Store";
import { type FieldConfig, createLabeledInput } from "./formHelpers";
import { navigateTo } from "../../utils/router";
import { companyDashboard } from "../../pages/companyDashboard/companyDashboard";

export function registrationFormCompany(): HTMLElement {
	const form: HTMLFormElement = document.createElement("form");

	const fields: FieldConfig[] = [
		{
			label: "Nome",
			type: "text",
			name: "name",
			required: true,
			minLength: 3,
		},
		{ label: "Email", type: "email", name: "email", required: true },
		{ label: "Estado", type: "text", name: "state", required: true },
		{ label: "País", type: "text", name: "country", required: true },
		{
			label: "CEP (12345-123)",
			type: "text",
			name: "cep",
			required: true,
			pattern: "\\d{5}-?\\d{3}",
		},
		{
			label: "CNPJ (14 digitos)",
			type: "text",
			name: "cnpj",
			required: true,
			pattern: "\\d{14}",
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

	const submitButton: HTMLButtonElement = document.createElement("button");
	submitButton.type = "submit";
	submitButton.textContent = "Cadastrar Empresa";
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

		const company: Company = new Company(
			store.getNextCompanyId(),
			inputMap["name"].value,
			inputMap["email"].value,
			inputMap["state"].value,
			inputMap["cep"].value,
			inputMap["description"].value,
			inputMap["cnpj"].value,
			inputMap["country"].value
		);
		store.addCompany(company);
		navigateTo(companyDashboard(company));
	});

	return form;
}
