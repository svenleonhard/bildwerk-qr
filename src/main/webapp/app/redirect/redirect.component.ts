import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserQrCodeExposedService } from 'app/entities/user-qr-code-exposed/user-qr-code-exposed.service';

@Component({
  selector: 'jhi-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['redirect.component.scss'],
})
export class RedirectComponent implements OnInit {
  message: string;

  constructor(private route: ActivatedRoute, private userQrCodeExposedService: UserQrCodeExposedService) {
    this.message = 'RedirectComponent message';
  }

  ngOnInit(): void {
    const code = this.route.snapshot.paramMap.get('code');
    if (code != null) {
      this.userQrCodeExposedService.find(code).subscribe(res => {
        if (res.body != null && res.body.url != null) {
          window.location.href = res.body.url;
        }
      });
    }
  }
}
