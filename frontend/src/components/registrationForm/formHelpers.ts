import styles from "./registrationForm.module.css";

export interface FieldConfig {
	label: string;
	type: string;
	name: string;
	required?: boolean;
	minLength?: number;
	pattern?: string;
	min?: number;
}

export function createLabeledInput(field: FieldConfig): HTMLDivElement {
	const wrapper: HTMLDivElement = document.createElement("div");
	wrapper.className = styles.formField;

	const label: HTMLLabelElement = document.createElement("label");
	label.textContent = field.label;
	label.setAttribute("for", field.name);

	let input: HTMLInputElement | HTMLTextAreaElement;
	if (field.name === "description") {
		input = document.createElement("textarea");
		input.name = field.name;
		input.id = field.name;
		if (field.required) input.required = true;
		if (field.minLength)
			(input as HTMLTextAreaElement).minLength = field.minLength;
	} else {
		input = document.createElement("input");
		input.type = field.type;
		input.name = field.name;
		input.id = field.name;
		if (field.required) input.required = true;
		if (field.minLength) input.minLength = field.minLength;
		if (field.pattern) input.pattern = field.pattern;
		if (field.min !== undefined) input.min = String(field.min);
	}

	wrapper.appendChild(label);
	wrapper.appendChild(input);

	return wrapper;
}
