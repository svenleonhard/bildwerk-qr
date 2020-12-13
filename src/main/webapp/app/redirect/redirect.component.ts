import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['redirect.component.scss'],
})
export class RedirectComponent implements OnInit {
  message: string;

  constructor() {
    this.message = 'RedirectComponent message';
  }

  ngOnInit(): void {}
}
