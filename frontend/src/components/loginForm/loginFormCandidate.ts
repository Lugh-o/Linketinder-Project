import type { Candidate } from "../../types/Candidate";
import type { AppContext } from "../../utils/AppContext";
import { generateCommonLoginForm } from "./loginFormHelper";

export function loginFormCandidate(appContext: AppContext): HTMLElement {
	return generateCommonLoginForm(
		"Entrar como candidato",
		() => appContext.apiGateway.getAllCandidates(),
		(candidate: Candidate) =>
			appContext.router.goToCandidateDashboard(candidate, appContext)
	);
}
