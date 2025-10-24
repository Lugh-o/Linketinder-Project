import type { Competency } from "./Competency";

export class Job {
	id?: number;
	name: string;
	description: string;
	competencies: Competency[];

	constructor(
		name: string,
		description: string,
		competencies: Competency[],
		id?: number
	) {
		this.id = id ?? 0;
		this.name = name;
		this.description = description;
		this.competencies = competencies;
	}
}
