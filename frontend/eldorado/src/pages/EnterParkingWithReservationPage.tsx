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
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card.tsx";
import {Pathnames} from "@/router/pathnames.ts";
import {useNavigate} from "react-router-dom";
import EnterParkingWithReservationForm from "@/components/forms/EnterParkingWithReservationForm.tsx";

function EnterParkingWithReservationPage() {
    const {t} = useTranslation();
    const navigate = useNavigate();

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer"
                                            onClick={() => navigate(Pathnames.public.home)}>{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{"Enter Reservation"}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <div className="mx-auto grid w-full max-w-4xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">Enter Parking with Reservation</h1>
                <Card className={"mt-5"}>
                    <CardHeader>
                        <CardTitle>Enter Your Reservation ID</CardTitle>
                        <CardDescription>Please provide your reservation ID to enter the parking</CardDescription>
                    </CardHeader>
                    <CardContent>
                        <EnterParkingWithReservationForm/>
                    </CardContent>
                </Card>
            </div>
        </div>
    );
}

export default EnterParkingWithReservationPage;
