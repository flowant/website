import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';

import { AppModule } from '../app.module';
import { AuthService, BackendService } from '../_services';
import { Config } from '../config';
import { SearchContentComponent } from './search-content.component';
import { Content, replacer, WebSite } from '../_models';

describe('SearchContentComponent', () => {
  let component: SearchContentComponent;
  let fixture: ComponentFixture<SearchContentComponent>;
  let httpTestingController: HttpTestingController;
  let logger: NGXLogger;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ],
      imports: [
        AppModule,
        HttpClientTestingModule,
        LoggerModule.forRoot({
          level: NgxLoggerLevel.TRACE,
          serverLogLevel: NgxLoggerLevel.TRACE
        })
      ],
      providers: [
        AuthService,
        BackendService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    httpTestingController = TestBed.get(HttpTestingController);
    logger = TestBed.get(NGXLogger);

    fixture = TestBed.createComponent(SearchContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    let contents: Content[] = [Content.random()];
    let cid = contents[0].idCid.containerId;
    let webSite = {
      identity: "f1b8dba2-44a4-11e9-944f-99e89c6a8c79",
      contentContainerIds: { recipe: "56a1cd50-3c77-11e9-bf26-d571c84212ed" },
      popularTagCounts: null
    }

    httpTestingController.expectOne(
      req => {
        return req.method.toUpperCase() === 'GET'
            && req.urlWithParams.includes(Config.webSiteUrl + "/" + webSite.identity);
      },
      Config.webSiteUrl + "/" + Config.webSite.identity
    ).flush(JSON.stringify(webSite, replacer));

    // httpTestingController.expectOne(
    //   req => {
    //     return req.method.toUpperCase() === 'GET'
    //         && req.urlWithParams.includes(Config.contentUrl + Config.path.popular + "?" + 'cid=' + cid);
    //   },
    //   Config.contentUrl + Config.path.popular + "?" + 'cid=' + cid
    // ).flush(JSON.stringify(contents, replacer));

    // return component;
    // httpTestingController.expectOne(
    //   req => {
    //     return req.method.toUpperCase() === 'GET'
    //         && req.urlWithParams.includes(Config.contentUrl + "?" + 'cid=' + cid);
    //   },
    //   Config.contentUrl + "?" + 'cid=' + cid
    // ).flush(JSON.stringify(contents, replacer));

  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    fixture.detectChanges();
    logger.trace("webSite info:", component.webSite);
  });

  // it('getPopularContents', () => {
  //   expect(component).toBeTruthy();

  //   component.getPopularContents().then(contents => {
  //     logger.trace("contents:", contents);
  //   });

  //   let contents: Content[] = [Content.random()];
  //   let cid = contents[0].idCid.containerId;

  //   httpTestingController.expectOne(
  //     req => {
  //       return req.method.toUpperCase() === 'GET'
  //           && req.urlWithParams.includes(Config.contentUrl + "?" + 'cid=' + cid);
  //     },
  //     Config.contentUrl + "?" + 'cid=' + cid
  //   ).flush(JSON.stringify(contents, replacer));

  //   httpTestingController.expectOne(
  //     req => {
  //       return req.method.toUpperCase() === 'GET'
  //           && req.urlWithParams.includes(Config.contentUrl + Config.path.popular + "?" + 'cid=' + cid);
  //     },
  //     Config.contentUrl + Config.path.popular + "?" + 'cid=' + cid
  //   ).flush(JSON.stringify(contents, replacer));

  // });

});
