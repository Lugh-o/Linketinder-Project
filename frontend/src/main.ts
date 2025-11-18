import "./styles/main.css";

import { registrationScreen } from "./pages/registrationScreen/registrationScreen";
import { Router } from "./utils/Router";
import { AppContext } from "./utils/AppContext";
import { ApiGateway } from "./utils/ApiGateway";

const appContainer: HTMLBodyElement | null = document.querySelector("body");
const router: Router = new Router();
const apiGateway: ApiGateway = new ApiGateway();
const appContext: AppContext = new AppContext(apiGateway, router);

if (appContainer) {
	appContainer.innerHTML = "";
	appContainer.appendChild(registrationScreen(appContext));
}
