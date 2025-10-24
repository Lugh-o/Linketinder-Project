import { Company } from "../../types/Company";
import { generateCommonLoginForm } from "./loginFormHelper";
import type { AppContext } from "../../utils/AppContext";

export function loginFormCompany(appContext: AppContext): HTMLElement {
	return generateCommonLoginForm(
		"Entrar como empresa",
		() => appContext.store.getCompanyList(),
		(company: Company) =>
			appContext.router.goToCompanyDashboard(company, appContext)
	);
}
