import { Candidate } from "../../types/Candidate";
import { Company } from "../../types/Company";
import { Person } from "../../types/Person";

export function generateCommonLoginForm<T extends Person>(
	buttonText: string,
	getList: () => Promise<T[]>,
	navigateTo: (person: T) => void
) {
	const form = document.createElement("form");

	const select = document.createElement("select");
	select.name = buttonText;
	select.required = true;

	let cachedList: T[] = [];

	getList().then((list) => {
		cachedList = list;

		list.forEach((p: T) => {
			const option = document.createElement("option");
			if (p instanceof Candidate) {
				option.value = p.idCandidate.toString();
				option.textContent = p.firstName + " " + p.lastName;
			} else if (p instanceof Company) {
				option.value = p.idCompany.toString();
				option.textContent = p.name;
			}

			select.appendChild(option);
		});
	});

	form.appendChild(select);

	const submitBtn = document.createElement("button");
	submitBtn.type = "submit";
	submitBtn.textContent = buttonText;
	form.appendChild(submitBtn);

	form.addEventListener("submit", (event) => {
		event.preventDefault();
		const selectedId = parseInt(select.value);

		const selectedPerson = cachedList.find((p) => {
			if (p instanceof Candidate) {
				return p.idCandidate === selectedId;
			} else if (p instanceof Company) {
				return p.idCompany === selectedId;
			}
			return false;
		});

		if (selectedPerson) {
			navigateTo(selectedPerson);
		}
	});

	return form;
}
