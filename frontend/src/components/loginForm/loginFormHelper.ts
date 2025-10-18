import { Person } from "../../types/Person";

export function generateCommonLoginForm<T extends Person>(
	buttonText: string,
	getList: () => T[],
	navigateTo: (person: T) => void
) {
	const form = document.createElement("form");

	const select = document.createElement("select");
	select.name = buttonText;
	select.required = true;

	getList().forEach((p: T) => {
		const option = document.createElement("option");
		option.value = p.id.toString();
		option.textContent = p.name;
		select.appendChild(option);
	});

	form.appendChild(select);

	const submitBtn = document.createElement("button");
	submitBtn.type = "submit";
	submitBtn.textContent = buttonText;
	form.appendChild(submitBtn);

	form.addEventListener("submit", (event) => {
		event.preventDefault();
		const selectedId = parseInt(select.value);
		const selectedPerson = getList().find((p) => p.id === selectedId);
		if (selectedPerson) {
			navigateTo(selectedPerson);
		}
	});

	return form;
}
