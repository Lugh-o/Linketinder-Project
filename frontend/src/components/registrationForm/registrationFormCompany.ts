import { Company } from "../../types/Company";
import { type FieldConfig, createLabeledInput } from "./formHelpers";
import { AppContext } from "../../utils/AppContext";
import { Address } from "../../types/Address";

export function registrationFormCompany(appContext: AppContext): HTMLElement {
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
		{
			label: "Senha",
			type: "password",
			name: "passwd",
			required: true,
			minLength: 3,
		},
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
			label: "CNPJ (12.123.123/1234-12)",
			type: "text",
			name: "cnpj",
			required: true,
			pattern: "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}",
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

		const address: Address = new Address(
			inputMap["state"].value,
			inputMap["postalCode"].value.replace("-", ""),
			inputMap["country"].value,
			inputMap["city"].value,
			inputMap["street"].value
		);

		const company: Company = new Company(
			0,
			0,
			inputMap["email"].value,
			inputMap["passwd"].value,
			inputMap["description"].value,
			address,
			inputMap["cnpj"].value
				.replaceAll(".", "")
				.replaceAll("-", "")
				.replaceAll("/", ""),
			inputMap["name"].value
		);
		appContext.apiGateway.createCompany(company);
		appContext.router.goToCompanyDashboard(company, appContext);
	});

	return form;
}
