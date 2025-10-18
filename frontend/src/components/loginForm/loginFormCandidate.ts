import { store } from "../../Store";
import { candidateDashboard } from "../../pages/candidateDashboard/candidateDashboard";
import type { Candidate } from "../../types/Candidate";
import { navigateTo } from "../../utils/router";
import { generateCommonLoginForm } from "./loginFormHelper";

export function loginFormCandidate(): HTMLElement {
	return generateCommonLoginForm(
		"Entrar como candidato",
		() => store.getCandidateList(),
		(candidate: Candidate) => navigateTo(candidateDashboard(candidate))
	);
}
