import type { ApiGateway } from "./ApiGateway";
import type { Router } from "./Router";

export class AppContext {
	apiGateway: ApiGateway;
	router: Router;

	constructor(apiGateway: ApiGateway, router: Router) {
		this.apiGateway = apiGateway;
		this.router = router;
	}
}
