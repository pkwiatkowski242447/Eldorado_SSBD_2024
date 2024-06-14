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
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";
import {
    AlertDialog,
    AlertDialogContent,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogCancel,
    AlertDialogAction,
    AlertDialogTitle,
    AlertDialogDescription
} from "@/components/ui/alert-dialog.tsx";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card.tsx";
import { Input } from "@/components/ui/input.tsx";
import {Pathnames} from "@/router/pathnames.ts";
import {useNavigate} from "react-router-dom";

function EnterParkingWithReservationPage() {
    const {t} = useTranslation();
    const [reservationID, setReservationID] = useState("");
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const navigate = useNavigate();

    const handleEnterReservation = async () => {
        if (reservationID) {
            try {
                await api.enterParkingWithReservation(reservationID);
                setIsDialogOpen(false);
                setIsResultDialogOpen(true);
            } catch (error) {
                // @ts-expect-error not now
                handleApiError(error);
            }
        }
    };

    const [isResultDialogOpen, setIsResultDialogOpen] = useState(false);

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer" onClick={() => navigate(Pathnames.public.home)}>{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrum.enter.reservation")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <div className="mx-auto grid w-full max-w-4xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">{t("enter.parking.without.reservation.page.enter.parking.with.reservation")}</h1>
                <Card className={"mt-5"}>
                    <CardHeader>
                        <CardTitle>{t("enter.parking.without.reservation.page.enter.your.reservation.id"}</CardTitle>
                        <CardDescription>{t("enter.parking.without.reservation.page.please,provide,your,reservation.id.to.enter.parking")}</CardDescription>
                    </CardHeader>
                    <CardContent>
                        <Input
                            placeholder="Reservation ID"
                            value={reservationID}
                            onChange={(e) => setReservationID(e.target.value)}
                        />
                        <Button onClick={() => setIsDialogOpen(true)} className="mt-4">
                            {t("enter.parking.without.reservation.page.submit")
                        </Button>
                    </CardContent>
                </Card>
            </div>
            <AlertDialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>{t("enter.parking.without.reservation.page.enter.reservation")}</AlertDialogTitle>
                        <AlertDialogDescription>
                            {t("enter.parking.without.reservation.page.are.you.sure")}
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogCancel onClick={() => setIsDialogOpen(false)}>{t("enter.parking.without.reservation.page.no")}</AlertDialogCancel>
                        <AlertDialogAction onClick={handleEnterReservation}>{t("enter.parking.without.reservation.page.yes")</AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
            <AlertDialog open={isResultDialogOpen} onOpenChange={setIsResultDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>{t("enter.parking.without.reservation.page.parking.has.been.entered.successfully")</AlertDialogTitle>
                        <AlertDialogDescription>
                            :)
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogAction onClick={() => setIsResultDialogOpen(false)}>OK</AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </div>
    );
}

export default EnterParkingWithReservationPage;
