import { store } from "../../Store";
import { companyDashboard } from "../../pages/companyDashboard/companyDashboard";
import { Company } from "../../types/Company";
import { navigateTo } from "../../utils/router";
import { generateCommonLoginForm } from "./loginFormHelper";

export function loginFormCompany(): HTMLElement {
	return generateCommonLoginForm(
		"Entrar como empresa",
		() => store.getCompanyList(),
		(company: Company) => navigateTo(companyDashboard(company))
	);
}
