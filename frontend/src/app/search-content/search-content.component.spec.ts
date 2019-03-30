import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
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

  let webSite = {
    identity: "f1b8dba2-44a4-11e9-944f-99e89c6a8c79",
    contentContainerIds: { recipe: "56a1cd50-3c77-11e9-bf26-d571c84212ed" },
    popularTagCounts: null
  }

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

  beforeEach(async(() => {
    httpTestingController = TestBed.get(HttpTestingController);
    logger = TestBed.get(NGXLogger);
    fixture = TestBed.createComponent(SearchContentComponent);
    component = fixture.componentInstance;
  }));

  beforeEach(async(() => {
    fixture.detectChanges();
    httpTestingController.expectOne(
      req => {
        return req.method.toUpperCase() === 'GET'
            && req.urlWithParams.includes(Config.webSiteUrl + "/" + webSite.identity);
      },
      Config.webSiteUrl + "/" + Config.webSite.identity
    ).flush(JSON.stringify(webSite, replacer));
  }));

  beforeEach(async(() => {
    fixture.detectChanges();
    let populars: Content[] = [Content.random()];
    httpTestingController.expectOne(
      req => {
        return req.method.toUpperCase() === 'GET'
            && req.urlWithParams.includes(Config.contentUrl + Config.path.popular
                + '?cid=' + webSite.contentContainerIds['recipe']);
      },
      Config.contentUrl + Config.path.popular + '?cid=' + webSite.contentContainerIds['recipe']
    ).flush(JSON.stringify(populars, replacer));
  }));

  beforeEach(async(() => {
    let latests: Content[] = [Content.random()];
    httpTestingController.expectOne(
      req => {
        return req.method.toUpperCase() === 'GET'
            && req.urlWithParams.includes(Config.contentUrl + '?cid=' + webSite.contentContainerIds['recipe']);
      },
      Config.contentUrl + '?cid=' + webSite.contentContainerIds['recipe']
    ).flush(JSON.stringify(latests, replacer));
  }));

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.webSite).toBeTruthy();
    expect(component.contents.length).toBeGreaterThan(0);
  });

  it('getPopularContents', () => {
    let latest: Content[] = [Content.random()];
    component.getNextLatest().then(latest => {
      latest.map(e => expect(component.contents).toContain(e));
    });

    httpTestingController.expectOne(
      req => {
        return req.method.toUpperCase() === 'GET'
            && req.urlWithParams.includes(Config.contentUrl + '?cid=' + webSite.contentContainerIds['recipe']);
      },
      Config.contentUrl + '?cid=' + webSite.contentContainerIds['recipe']
    ).flush(JSON.stringify(latest, replacer));
  });

});
