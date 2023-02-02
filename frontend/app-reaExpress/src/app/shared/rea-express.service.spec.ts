import { TestBed } from '@angular/core/testing';

import { ReaExpressService } from './rea-express.service';

describe('ReaExpressService', () => {
  let service: ReaExpressService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReaExpressService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
