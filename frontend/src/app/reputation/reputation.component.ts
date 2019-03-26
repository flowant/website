import { Component, OnInit, Input } from '@angular/core';
import { BackendService } from '../_services';
import { IdCid, Reputation, Content } from '../_models';
import { NGXLogger } from 'ngx-logger';

const numeral = require('numeral');

@Component({
  selector: 'app-reputation',
  templateUrl: './reputation.component.html',
  styleUrls: ['./reputation.component.scss']
})
export class ReputationComponent implements OnInit {

  @Input() typeName: string;
  @Input() idCid: IdCid;
  @Input() reputation: Reputation;

  selected: string;

  constructor(
    private backendService: BackendService,
    private logger: NGXLogger) { }

  ngOnInit() {
  }

  isRated(): boolean {
    return Content.name === this.typeName;
  }

  getRated(): string {
    let avr = this.reputation.rated / this.reputation.reputed;
    return avr.toFixed(1);
  }

  onRepute(idCid: IdCid, selected: string) {
    // TODO check User Id and already clicked by user
    let reputation = new Reputation();
    reputation.select(selected);
    reputation.idCid = idCid;

    this.backendService.acculateRepute(this.typeName, idCid, reputation)
        .subscribe((rpt) => {this.reputation = rpt});
  }

  toNumberal(num: number): string {
    return numeral(num).format('0a').toUpperCase();
  }
}
