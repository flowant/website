import { Component, OnInit, Input } from '@angular/core';
import { Config } from '../config';
import { BackendService } from '../_services';
import { ActivatedRoute, Router } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
import { Content } from '../_models';

@Component({
  selector: 'app-content-ref',
  templateUrl: './content-ref.component.html',
  styleUrls: ['./content-ref.component.scss']
})
export class ContentRefComponent implements OnInit {

  @Input() content: Content;

  imgServerUrl: string = Config.gatewayUrl;

  constructor(
    private backendService: BackendService,
    private router: Router,
    private route: ActivatedRoute,
    private logger: NGXLogger
  ) { }

  ngOnInit() {
  }

  onClick() {
    this.logger.trace("ContentRef onClick, idCid:", this.content.idCid);
    this.router.navigate(['/content/view/', this.content.idCid.identity, this.content.idCid.containerId]);
  }

}
