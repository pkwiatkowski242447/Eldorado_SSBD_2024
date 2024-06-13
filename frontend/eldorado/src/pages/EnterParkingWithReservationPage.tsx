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
                        <Input
                            placeholder="Reservation ID"
                            value={reservationID}
                            onChange={(e) => setReservationID(e.target.value)}
                        />
                        <Button onClick={() => setIsDialogOpen(true)} className="mt-4">
                            Submit
                        </Button>
                    </CardContent>
                </Card>
            </div>
            <AlertDialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>Enter Reservation</AlertDialogTitle>
                        <AlertDialogDescription>
                            Are you sure you want to enter the parking with this reservation ID?
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogCancel onClick={() => setIsDialogOpen(false)}>No</AlertDialogCancel>
                        <AlertDialogAction onClick={handleEnterReservation}>Yes</AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
            <AlertDialog open={isResultDialogOpen} onOpenChange={setIsResultDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>Parking has been entered successfully!</AlertDialogTitle>
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
