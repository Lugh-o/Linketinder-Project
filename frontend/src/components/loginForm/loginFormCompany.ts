import { store } from "../../Store";
import { companyDashboard } from "../../pages/companyDashboard/companyDashboard";
import { navigateTo } from "../../utils/router";

export function loginFormCompany(): HTMLElement {
	const form = document.createElement("form");

	const select = document.createElement("select");
	select.name = "company";
	select.required = true;

	store.getCompanyList().forEach((company) => {
		const option = document.createElement("option");
		option.value = company.id.toString();
		option.textContent = company.name;
		select.appendChild(option);
	});

	form.appendChild(select);

	const submitBtn = document.createElement("button");
	submitBtn.type = "submit";
	submitBtn.textContent = "Entrar como Empresa";
	form.appendChild(submitBtn);

	form.addEventListener("submit", (e) => {
		e.preventDefault();
		const selectedId = Number(select.value);
		const company = store.getCompanyList().find((c) => c.id === selectedId);
		if (company) {
			navigateTo(companyDashboard(company));
		}
	});

	return form;
}
