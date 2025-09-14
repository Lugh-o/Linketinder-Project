import type { Competency } from "./Competency";

export class Job {
	id: number;
	name: string;
	description: string;
	competencies: Competency[];

	constructor(
		id: number,
		name: string,
		description: string,
		competencies: Competency[]
	) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.competencies = competencies;
	}
}
