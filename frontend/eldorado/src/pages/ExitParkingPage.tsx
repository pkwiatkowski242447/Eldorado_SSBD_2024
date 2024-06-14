import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Pathnames} from "@/router/pathnames.ts";
import {Slash} from "lucide-react";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {useAccount} from "@/hooks/useAccount.ts";
import ExitParkingForm from "@/components/forms/ExitParkingForm.tsx";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {Button} from "@/components/ui/button.tsx";

export function ExitParkingPage(){
    const {t} = useTranslation();
    const navigate = useNavigate();
    const {isAuthenticated} = useAccount();
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
                            <BreadcrumbLink>{t("breadcrumb.exitParking")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <div className="mx-auto grid w-full max-w-4xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">{t("breadcrumb.exitParking")}</h1>
                <Card className={"mt-5"}>
                    <CardHeader>
                        <CardTitle>{t("exit.parking.form.id.of.reservation")}</CardTitle>
                        <CardDescription>{t("exit.parking.form.end.reservation")}</CardDescription>
                    </CardHeader>
                    <CardContent>
                        <ExitParkingForm isAuthenticated={isAuthenticated}/>
                    </CardContent>
                </Card>
            </div>
        </div>
    )
}