import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['redirect.component.scss'],
})
export class RedirectComponent implements OnInit {
  message: string;

  constructor(private route: ActivatedRoute) {
    this.message = 'RedirectComponent message';
  }

  ngOnInit(): void {
    window.location.href = 'https://google.de';
  }
}
