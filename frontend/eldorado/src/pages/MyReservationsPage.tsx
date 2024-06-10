import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Slash} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {useTranslation} from "react-i18next";

import {useState} from "react";
import MyHistoricalReservationsPage from "@/pages/MyHistoricalReservationsPage.tsx";
import MyActiveReservationsPage from "@/pages/MyActiveReservationsPage.tsx";

function MyReservationsPage() {
    const [activePage, setActivePage] = useState("Historical");
    const {t} = useTranslation();

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink href="/home">{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>My Reservations</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <div className={"flex-auto"}>
                <Button variant={`${activePage === 'Historical' ? 'secondary' : 'ghost'}`}
                        onClick={() => setActivePage('Historical')}
                        className={`text-muted-foreground transition-colors hover:text-foreground`}>
                    Historical
                </Button>
                <Button variant={`${activePage === 'Active' ? 'secondary' : 'ghost'}`}
                        onClick={() => setActivePage('Active')}
                        className={`text-muted-foreground transition-colors hover:text-foreground`}>
                    Active
                </Button>
            </div>
            <div className={"grid gap-6"}>
                {activePage === 'Historical' && (
                    <MyHistoricalReservationsPage/>
                )}
                {activePage === 'Active' && (
                    <MyActiveReservationsPage/>
                )}
            </div>
        </div>

    );
}

export default MyReservationsPage;