import {Component, Input} from '@angular/core';

@Component({
    selector: 'app-spinner',
    standalone: true,
    template: '<div class="loading"><span class="spinner"></span><span>{{label}}</span></div>'
})
export class SpinnerComponent {
    @Input() label = 'Loading...';
}

@Component({
    selector: 'app-status',
    standalone: true,
    template: '<span class="badge" [class]="statusClass">{{status}}</span>'
})
export class StatusComponent {
    @Input() status = '';

    get statusClass() {
        return 'badge ' + String(this.status).toLowerCase();
    }
}

@Component({
    selector: 'app-empty',
    standalone: true,
    template: '<div class="empty"><div class="empty-icon">◌</div><h3>{{title}}</h3><p>{{text}}</p></div>'
})
export class EmptyComponent {
    @Input() title = 'Nothing here yet';
    @Input() text = 'Your records will appear here.';
}
