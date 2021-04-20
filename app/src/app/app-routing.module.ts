import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
// import { ShipCreateComponent } from "./components/manage-ships/ship-create/ship-create.component";
import { ShipListComponent } from "./components/manage-ships/ship-list/ship-list.component";

const routes: Routes = [
    { path: '', redirectTo: 'ships', pathMatch: 'full' },
    // { path: 'ships', component: ShipListComponent },
    // { path: 'create', component: ShipCreateComponent }
]

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }