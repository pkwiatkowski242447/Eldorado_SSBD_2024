import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Loader2} from "lucide-react";
import {useTranslation} from "react-i18next";
import {Input} from "@/components/ui/input.tsx";
import {Card, CardContent} from "@/components/ui/card.tsx";
import {
    AlertDialog, AlertDialogAction, AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";

// @ts-expect-error idk
function EmailForm({formEmail, onSubmitEmail, isLoading, account, isAlertDialogOpen, setAlertDialogOpen, resendEmailConfirmation = () => {}, isLoadingReset=false, handleDialogAction, showResendButton}) {
    const {t} = useTranslation();

    return (
        <div>
            <Card className="mx-10 w-auto">
                <CardContent>
                    <Form {...formEmail}>
                            <form onSubmit={formEmail.handleSubmit(onSubmitEmail)}
                                  className="space-y-4">
                                <div className="grid gap-4 p-5">
                                    <div className="grid gap-2">
                                        <FormField
                                            control={formEmail.control}
                                            name="email"
                                            render={({field}) => (
                                                <FormItem>
                                                    <FormLabel
                                                        className="text-black">{t("accountSettings.authentication.email")} *</FormLabel>
                                                    <FormControl>
                                                        <Input
                                                            placeholder={account?.email} {...field} />
                                                    </FormControl>
                                                    <FormMessage/>
                                                </FormItem>
                                            )}/>
                                    </div>
                                    <Button type="submit" className="w-full pb-2"
                                            disabled={isLoading}>
                                        {isLoading ? (
                                            <>
                                                <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                            </>
                                        ) : (
                                            t("accountSettings.authentication.email.change")
                                        )}
                                    </Button>
                                </div>
                            </form>

                    </Form>
                </CardContent>
                {showResendButton && (
                    <div className={"flex mb-5 justify-center "}>
                        <Button onClick={resendEmailConfirmation} variant={"outline"} className="w-auto pb-2"
                                disabled={isLoadingReset}>
                            {isLoadingReset ? (
                                <>
                                    <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                </>
                            ) : (
                                t("accountSettings.popUp.resendEmailConfirmationOK.button")
                            )}
                        </Button>
                    </div>
                )}
            </Card>
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        {t("accountSettings.confirmEmailChange")}
                    </AlertDialogDescription>
                    <AlertDialogAction onClick={handleDialogAction}>
                        {t("general.ok")}
                    </AlertDialogAction>
                    <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                </AlertDialogContent>
            </AlertDialog>
        </div>
    );
}

export default EmailForm;