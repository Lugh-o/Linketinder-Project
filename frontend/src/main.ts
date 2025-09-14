import "./styles/main.css";

import { registrationScreen } from "./pages/registrationScreen/registrationScreen";

const appContainer: HTMLBodyElement | null = document.querySelector("body");

if (appContainer) {
	appContainer.innerHTML = "";
	appContainer.appendChild(registrationScreen());
}
